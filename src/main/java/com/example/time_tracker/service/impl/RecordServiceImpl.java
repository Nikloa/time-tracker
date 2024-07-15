package com.example.time_tracker.service.impl;

import com.example.time_tracker.model.Project;
import com.example.time_tracker.model.Record;
import com.example.time_tracker.model.User;
import com.example.time_tracker.model.dto.RecordDto;
import com.example.time_tracker.repository.ProjectRepository;
import com.example.time_tracker.repository.RecordRepository;
import com.example.time_tracker.repository.UserRepository;
import com.example.time_tracker.service.RecordService;
import com.example.time_tracker.util.convertor.RecordMapper;
import com.example.time_tracker.util.exception.ModelNotFoundException;
import com.example.time_tracker.util.exception.WrongDateOrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final RecordMapper recordMapper;

    //Метод используется пользователем для получения всех сделанных им записей
    @Override
    public List<RecordDto> findAll() {
        return recordMapper.toListDto(getCurrentUser().getRecords());
    }

    /*
    Метод используется пользователем для получения всех сделанных им записей
    по конкретному, назначенному для него, проекту.
    В начале метод проверяет назначен ли текущему пользователю проект с этим id
    Если нет, генерируется исключение,
    если да, происходит запрос к базе и найденные записи возвращаются методом
     */
    @Override
    public List<RecordDto> findAllForCurrentUserByProjectId(Long id) {
        User user = getCurrentUser();
        Project project = user.getProjects().stream().filter(p -> p.getId().equals(id)).findFirst().orElseThrow(
                () -> new ModelNotFoundException("Current user has not project with id: " + id));
        return recordMapper.toListDto(recordRepository.findAllByUserAndProject(user, project));
    }

    //    Используется пользователем для получения сделанной им записи по id
    @Override
    public RecordDto findById(Long id) {
        return recordMapper.modelToDto(getRecordForCurrentUserById(id));
    }

//    Используется администратором для получения всех записей
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<RecordDto> findAllGenerally() {
        return recordMapper.toListDto(recordRepository.findAll());
    }

//    Используется администратором для получения всех записей пользователя
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<RecordDto> findAllByUserId(Long id) {
        return recordMapper.toListDto(userRepository.findById(id).orElseThrow(
                () -> new ModelNotFoundException("User with id: " + id + " not found")).getRecords());
    }

//    Используется администратором для получения всех записей проекта
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<RecordDto> findAllByProjectId(Long id) {
        return recordMapper.toListDto(projectRepository.findById(id).orElseThrow(
                () -> new ModelNotFoundException("Project with id: " + id + " not found")).getRecords());
    }

    /*
    Создает новую запись по проекту и текущему пользователю.
    Сначала происходит проверка времени,
    время начала должно быть раньше времени окончания,
    если это не так генерируется исключение.
    Далее происходит получение текущего пользователя и проверка,
    назначен ли ему проект с данным id,если нет выбрасывается исключение
    После чего записи назначаются пользователь и проект к которым эта запись относится,
    и запись сохраняется в базе.
     */
    @Override
    @Transactional
    public RecordDto createByProjectId(Long id, RecordDto recordDto) {
        if (!recordDto.getEndTime().after(recordDto.getStartTime()))
            throw new WrongDateOrderException("Start time should be before end time");
        User user = getCurrentUser();
        Project project = user.getProjects().stream().filter(p -> p.getId().equals(id)).findFirst().orElseThrow(
                () -> new ModelNotFoundException("Current user has not project with id: " + id));
        Record record = recordMapper.dtoToModel(recordDto);
        record.setUser(user);
        record.setProject(project);
        user.getRecords().add(record);
        project.getRecords().add(record);
        recordRepository.save(record);
        return recordMapper.modelToDto(record);
    }

    /*
    Обновляет запись созданную текущим пользователем.
    Сначала происходит проверка времени,
    время начала должно быть раньше времени окончания,
    если это не так генерируется исключение.
    Далее происходит получение записи текущего пользователя и проверка,
    если запись с данным id сделана не текущим пользователем выбрасывается исключение.
    После чего новой записи назначаются id, пользователь и проект старой записи,
    и запись сохраняется в базе.
     */
    @Override
    @Transactional
    public RecordDto updateById(Long id, RecordDto recordDto) {
        if (!recordDto.getEndTime().after(recordDto.getStartTime()))
            throw new WrongDateOrderException("Start time should be before end time");
        Record record = getRecordForCurrentUserById(id);
        if (recordRepository.existsById(id)) {
            Record updated = recordMapper.dtoToModel(recordDto);
            updated.setId(id);
            updated.setUser(record.getUser());
            updated.setProject(record.getProject());
            return recordMapper.modelToDto(recordRepository.save(updated));
        } else {
            throw new ModelNotFoundException("Record with id: " + id + " not found");
        }
    }

    /*
    Метод удаляет запись по id
    Сначала происходит проверка что запись с данным id сделана текущим пользователем
    Если это не так генерируется исключение, если проверка пройдена, запись удаляется
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = getCurrentUser();
        Record record = getRecordForCurrentUserById(id);
        user.getRecords().remove(record);
        recordRepository.delete(recordRepository.findById(id).orElseThrow(
                () -> new ModelNotFoundException("Record with id: " + id + " not found")));
    }

//    Получение текущего авторизованного пользователя из контекста
    private User getCurrentUser() {
        return userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(
                () -> new UsernameNotFoundException("Authenticated user not found"));
    }

//    Получение записи у текущего пользователя по её id
//    Если запись с этим id создана не текущим пользователем генерируется исключение
    private Record getRecordForCurrentUserById(Long id) {
        return getCurrentUser().getRecords().stream().filter(r -> r.getId().equals(id)).findFirst().orElseThrow(
                () -> new ModelNotFoundException("Current user has not record with id: " + id));
    }
}

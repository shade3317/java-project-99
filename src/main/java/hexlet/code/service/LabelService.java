package hexlet.code.service;
import hexlet.code.dto.label.LabelCreateDto;
import hexlet.code.dto.label.LabelDto;
import hexlet.code.dto.label.LabelUpdateDto;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class LabelService {
    private final LabelRepository labelRepository;

    private final LabelMapper     labelMapper;

    public List<LabelDto> getAll() {
        var labels = labelRepository.findAll();
        return labels.stream()
                .map(labelMapper::map)
                .toList();
    }

    public LabelDto getById(Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Метка с id " + id + " не найдена"));
        return labelMapper.map(label);
    }

    public LabelDto create(LabelCreateDto dto) {
        var label = labelMapper.map(dto);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public LabelDto update(LabelUpdateDto dto, Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Метка с id " + id + " не найдена"));
        labelMapper.update(dto, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public void delete(Long id) {
        labelRepository.deleteById(id);
    }
}

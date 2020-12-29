package com.salari.framework.uaa.model.dto.base;
import com.salari.framework.uaa.utility.ApplicationProperties;
import java.util.HashSet;
import java.util.Set;

public class MetaDTO {

    Set<MetaMapDTO> success;
    Set<MetaMapDTO> warnings;
    Set<MetaMapDTO> errors;

    public MetaDTO() {
    }

    public MetaDTO(Set<MetaMapDTO> warnings, Set<MetaMapDTO> errors) {
        this.warnings = warnings;
        this.errors = errors;
    }

    public MetaDTO(Set<MetaMapDTO> success, Set<MetaMapDTO> warnings, Set<MetaMapDTO> errors) {
        this.success = success;
        this.warnings = warnings;
        this.errors = errors;
    }

    public static MetaDTO getInstance(){
        Set<MetaMapDTO> success=new HashSet<>();
        success.add(MetaMapDTO.builder().key("successful").message(ApplicationProperties.getProperty("successful")).build());
        return new MetaDTO(success,null,null);
    }
}

package com.salari.framework.uaa.model.dto.base;

import java.util.HashSet;
import java.util.Set;

public class BaseDTOMapper {

    public static BaseDTOMapper getInstance() {
        return new BaseDTOMapper();
    }

    public Set<MetaMapDTO> setMetaDTOCollection(Set<MetaMapDTO> set, MetaMapDTO metaMapDTO) {
        if (set == null)
            set = new HashSet<>();
        set.add(metaMapDTO);
        return set;
    }
}

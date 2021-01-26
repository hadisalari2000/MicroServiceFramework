package com.salari.framework.msuaa.utility;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageableUtility {

    public Pageable createPageable(Integer page) {
        if (page == null) return null;
        return PageRequest.of(
                page - 1,
                Integer.parseInt(ApplicationProperties.getProperty("application.page.size")),
                Sort.by("id").ascending()
        );
    }

    public Pageable createPageable(Integer page, Sort sort) {
        if (page == null) return null;
        return PageRequest.of(
                page - 1,
                Integer.parseInt(ApplicationProperties.getProperty("application.page.size")),
                sort
        );
    }

}

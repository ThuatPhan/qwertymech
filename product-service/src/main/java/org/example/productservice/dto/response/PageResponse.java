package org.example.productservice.dto.response;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> implements Serializable {
    List<T> data;
    int currentPage;
    int pageSize;
    int totalPages;
    long totalElements;

    public static <T> PageResponse<T> empty(int page, int size) {
        return PageResponse.<T>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(0)
                .totalPages(0)
                .data(Collections.emptyList())
                .build();
    }

    public static <E, R> PageResponse<R> from(Page<E> pageData, Function<E, R> mapper) {
        return PageResponse.<R>builder()
                .currentPage(pageData.getNumber() + 1) // Index start at 0
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.stream().map(mapper).toList())
                .build();
    }
}

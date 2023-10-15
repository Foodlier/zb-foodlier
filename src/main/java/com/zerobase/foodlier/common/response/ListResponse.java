package com.zerobase.foodlier.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListResponse<T> {

    private long totalElements;
    private int totalPages;
    private boolean hasNextPage;
    private List<T> content = new ArrayList<>();

    public static <T> ListResponse<T> from(Page<T> page){
        return ListResponse.<T>builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNextPage(page.hasNext())
                .content(page.getContent())
                .build();
    }

    public static <T, R> ListResponse<R> from(Page<T> page, Function<? super T, ? extends R> mapper) {
        return ListResponse.<R>builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNextPage(page.hasNext())
                .content(
                        page.getContent()
                                .stream()
                                .map(mapper)
                                .collect(Collectors.toList())
                )
                .build();
    }
}

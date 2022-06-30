package com.prgrms.ohouse.web.requests;

import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.commerce.application.command.ReviewRegisterCommand;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ReviewCreateRequest {
    private Long productId;
    private Long userId;
    private int reviewPoint;
    private String contents;

    public ReviewCreateRequest(Long productId, Long userId, int reviewPoint, String contents) {
        this.productId = productId;
        this.userId = userId;
        this.reviewPoint = reviewPoint;
        this.contents = contents;
    }

    public ReviewRegisterCommand toCommand(MultipartFile reviewImage) {
        return new ReviewRegisterCommand(productId, userId, reviewPoint, contents, reviewImage);
    }
}

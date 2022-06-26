package com.prgrms.ohouse.web.requests;

import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.commerce.application.command.ReviewRegisterCommand;

import lombok.Getter;

@Getter
public class ReviewCreateRequest {
    private final Long productId;
    private final Long userId;
    private final int reviewPoint;
    private final String contents;
    private MultipartFile reviewImage;

    public ReviewCreateRequest(Long productId, Long userId, int reviewPoint, String contents,
        MultipartFile reviewImage) {
        this.productId = productId;
        this.userId = userId;
        this.reviewPoint = reviewPoint;
        this.contents = contents;
        this.reviewImage = reviewImage;
    }

    public ReviewRegisterCommand toCommand() {
        return new ReviewRegisterCommand(productId, userId, reviewPoint, contents, reviewImage);
    }
}

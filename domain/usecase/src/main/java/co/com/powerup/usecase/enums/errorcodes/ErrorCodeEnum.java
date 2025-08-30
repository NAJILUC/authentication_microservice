package co.com.powerup.usecase.enums.errorcodes;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {


    //    User
    C01USER01("Email already exists", ErrorEnum.REQUEST_EXCEPTION.getValue(), "Email"),

    ;

    private final String code;

    private final String message;
    private final String description;
    private final String field;

    ErrorCodeEnum(String message, String description,  String field) {
        this.code = this.name();
        this.message = message;
        this.description = description;
        this.field = field;
    }
}

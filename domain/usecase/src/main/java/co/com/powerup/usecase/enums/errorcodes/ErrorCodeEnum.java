package co.com.powerup.usecase.enums.errorcodes;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {


    //    User
    C01USER01("Email already exists", ErrorEnum.REQUEST_EXCEPTION.getValue(), "Email"),
    C01USER02("User was not found", ErrorEnum.NOT_FOUND_EXCEPTION.getValue(), "Email"),

    //    Credit Type
    C01CRTY01("Credit type was not found", ErrorEnum.NOT_FOUND_EXCEPTION.getValue(), "Id"),

    //    Status
    C01STAT01("Status type was not found", ErrorEnum.NOT_FOUND_EXCEPTION.getValue(), "Id"),

    //    Login
    C01LOGI01("Email or password invalid", ErrorEnum.JWT_EXCEPTION.getValue(), "Id"),
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

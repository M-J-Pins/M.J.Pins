from pydantic import BaseModel, Field, constr, validator


class PhoneAuthRequestRequest(BaseModel):
    phone: constr(
        strip_whitespace=True,
        regex=r"^(\+)[1-9][0-9\-\(\)\.]{9,15}$",
    ) = Field(...)

    @validator("phone")
    def validate_phone(cls, phone):
        new_phone = [p if p.isdigit() else "" for p in phone]
        return "".join(new_phone)

    class Config:
        orm_mode = True


class PhoneAuthRequest(BaseModel):
    phone: constr(
        strip_whitespace=True,
        regex=r"^(\+)[1-9][0-9\-\(\)\.]{9,15}$",
    ) = Field(...)
    code: str = Field(..., min_length=4, max_length=4)

    @validator("phone")
    def validate_phone(cls, phone):
        new_phone = [p if p.isdigit() else "" for p in phone]
        return "".join(new_phone)

    class Config:
        orm_mode = True


class AuthResponse(BaseModel):
    token: str = Field(...)

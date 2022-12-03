from pydantic import BaseModel, Field


class BaseResponse(BaseModel):
    description: str = Field(...)

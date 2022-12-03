from pydantic import BaseModel


class AuthorizedRequest(BaseModel):
    token: str

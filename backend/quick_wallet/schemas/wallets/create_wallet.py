from uuid import UUID

from pydantic import constr, validator
from pydantic.fields import Field, List

from quick_wallet.schemas.base import AuthorizedRequest


class CreateWalletRequest(AuthorizedRequest):
    name: str = Field(..., max_length=25)
    cards: List[UUID]
    users: List[
        constr(
            strip_whitespace=True,
            regex=r"^(\+)[1-9][0-9\-\(\)\.]{9,15}$",
        )
    ]

    @validator("users")
    def validate_phone(cls, phone):
        new_phone = [p if p.isdigit() else "" for p in phone]
        return "".join(new_phone)

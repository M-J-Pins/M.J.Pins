from uuid import UUID

from pydantic.fields import Field, Optional

from quick_wallet.schemas.base import AdminRequest, AuthorizedRequest


class AddSimilarityRequest(AdminRequest):
    shop_id: UUID
    card_text: Optional[str] = Field(default="")
    card_color: Optional[str] = Field(default="")


class SimilarityRequest(AuthorizedRequest):
    card_text: str
    card_color: str

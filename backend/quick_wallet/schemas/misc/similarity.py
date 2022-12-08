from uuid import UUID

from pydantic.fields import Field, Optional

from quick_wallet.schemas.base import AdminRequest, AuthorizedRequest


class AddSimilarityRequest(AdminRequest):
    shop_id: UUID
    card_text: str
    card_color: str


class SimilarityRequest(AuthorizedRequest):
    card_text: str
    card_color: str

from pydantic import BaseModel
from quick_wallet.config import get_settings


class AdminRequest(BaseModel):
    token: str = get_settings().ADMIN_TOKEN

from pydantic import constr, validator

from quick_wallet.schemas.wallets import WalletId


class AccessToWalletRequest(WalletId):
    user_to_change_access_phone: constr(
        strip_whitespace=True,
        regex=r"^(\+)[1-9][0-9\-\(\)\.]{9,15}$",
    )

    @validator("user_to_change_access_phone")
    def validate_phone(cls, phone):
        new_phone = [p if p.isdigit() else "" for p in phone]
        return "".join(new_phone)

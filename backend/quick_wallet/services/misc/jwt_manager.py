import secrets
from datetime import datetime, timedelta, timezone
from uuid import UUID

import jwt
from singleton_decorator import singleton

from quick_wallet.utils.datetime import calculate_future_datetime


@singleton
class JWTManager:
    algo: str = "HS256"
    secret: str

    def __init__(self):
        self.reset_secret()

    def reset_secret(self):
        self.secret = self.generate_secret()

    @staticmethod
    def generate_secret():
        return secrets.token_hex()

    @staticmethod
    def generate_token():
        return secrets.token_hex()

    async def generate_jwt(
        self,
        id_to_jwt: UUID = None,
        data: dict = {},
        active_period: timedelta = timedelta(days=30),
    ) -> str:
        data["id"] = str(id_to_jwt)

        date_expire = calculate_future_datetime(active_period)
        data["exp"] = date_expire
        new_jwt = jwt.encode(data, self.secret, self.algo)
        return new_jwt

    def id_from_jwt(self, client_jwt: str) -> UUID:
        try:
            id_from_jwt = jwt.decode(
                client_jwt.encode(), self.secret, algorithms=[self.algo]
            ).get("id", None)
        except jwt.exceptions.DecodeError as e:
            print(e)
            id_from_jwt = None
        return id_from_jwt

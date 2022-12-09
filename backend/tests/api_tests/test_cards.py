from urllib.parse import urlencode

import pytest
from starlette import status
from quick_wallet.services.misc import JWTManager
from tests.prepared_data import valid_unknown_card, valid_standard_card

pytestmark = pytest.mark.asyncio


async def to_mock(*args, **kwargs):
    return


class TestCards:
    @staticmethod
    def get_url() -> str:
        return "/api/v1/cards"

    @pytest.mark.parametrize(
        "token, expected_status, card_id_list",
        [
            (JWTManager().generate_jwt("8c06da29-6198-4fbc-823a-fa3e47b94c64"), status.HTTP_200_OK, (
                    "0bca364e-efd0-4f37-aef9-ec401ddf7b51",
                    "3acf7853-1299-40df-a2fe-544304dd47e6",
                    "4e0abd75-c7c2-4252-aac8-aa967eed64a8",
                    "0062e29c-00c9-4ff1-b2e5-9b437b1bfeba",
                    "9eaada03-81d7-4ecc-b4d4-b99c163a9f7e",
            )),
            (JWTManager().generate_jwt("065381f8-953c-46ce-98f4-0e6625024160"), status.HTTP_200_OK, (
                    "6a48c56c-84a5-458b-89e5-7382a496a020",
                    "38861809-7fb2-4ddb-9db5-6fc8cae8a4c5",
                    "58c31f99-19e3-455c-911b-169859b6cab6",
            )),
            (JWTManager().generate_jwt("3a9b910b-207a-440f-8a79-ede85a0f5884"), status.HTTP_200_OK, (
                    "985be2ce-abd8-4c2a-b0a4-11de0e79c1ee",
            )),
            ("token", status.HTTP_401_UNAUTHORIZED, ()),
        ]
    )
    async def test_get_cards(self, client, prepare_users, prepare_cards, token, expected_status, card_id_list):

        try:
            token = await token
        except:
            pass
        response = await client.get(url=self.get_url() + "/my" + f"?token={token}")
        assert response.status_code == expected_status
        cards = [card.get('id') for card in
                 (response.json().get("cards") if response.json().get("cards") is not None else [])]
        assert set(cards) == set(card_id_list)

    async def test_add_standard_card(self, client, prepare_users, prepare_cards):

        headers = {"Content-Type": "application/json"}
        token = await JWTManager().generate_jwt("3a9b910b-207a-440f-8a79-ede85a0f5884")
        valid_standard_card["token"] = token
        response = await client.post(url=self.get_url() + "/standard", json=valid_standard_card, headers=headers)
        assert response.status_code == status.HTTP_200_OK
        response = await client.get(url=self.get_url() + "/my" + f"?token={token}")
        assert len(response.json().get("cards")) == 2

    async def test_delete_standard_card(self, client, prepare_users, prepare_cards):

        token = await JWTManager().generate_jwt("3a9b910b-207a-440f-8a79-ede85a0f5884")
        valid_standard_card["token"] = token
        response = await client.get(url=self.get_url() + "/my" + f"?token={token}")
        assert len(response.json().get("cards")) == 1
        print(response.json().get('cards')[0].get('id'))
        response = await client.delete(url=self.get_url() + f"/{response.json().get('cards')[0].get('id')}?token={token}")
        assert response.status_code == status.HTTP_200_OK
        response = await client.get(url=self.get_url() + "/my" + f"?token={token}")
        assert len(response.json().get("cards")) == 0

    async def test_add_unknown_card(self, client, prepare_users, prepare_cards):

        headers = {"Content-Type": "application/json"}
        token = await JWTManager().generate_jwt("3a9b910b-207a-440f-8a79-ede85a0f5884")
        valid_unknown_card["token"] = token
        response = await client.post(url=self.get_url() + "/unknown", json=valid_unknown_card, headers=headers)
        assert response.status_code == status.HTTP_200_OK
        response = await client.get(url=self.get_url() + "/my" + f"?token={token}")
        assert len(response.json().get("cards")) == 2
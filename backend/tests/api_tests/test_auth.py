import pytest
from starlette import status

pytestmark = pytest.mark.asyncio


class TestAuth:
    @staticmethod
    def get_url() -> str:
        return "/api/v1/auth"

    @pytest.mark.parametrize(
        "actions",
        (
            [
                {
                    "status": status.HTTP_202_ACCEPTED,
                    "route": "/auth_request",
                    "data": {
                        "phone": "+7(000)000-00-00",
                    },
                },
                {
                    "status": status.HTTP_423_LOCKED,
                    "route": "/auth_request",
                    "data": {
                        "phone": "+7(000)000-00-00",
                    },
                },
                {
                    "status": status.HTTP_423_LOCKED,
                    "route": "/auth_request",
                    "data": {
                        "phone": "+70000000000",
                    },
                },
                {
                    "status": status.HTTP_202_ACCEPTED,
                    "route": "/auth_request",
                    "data": {
                        "phone": "+70000000001",
                    },
                },
            ],
            [
                {
                    "status": status.HTTP_202_ACCEPTED,
                    "route": "/auth_request",
                    "data": {
                        "phone": "+70000000002",
                    },
                },
                {
                    "status": status.HTTP_404_NOT_FOUND,
                    "route": "/auth",
                    "data": {
                        "phone": "+70000000002",
                        "code": "0000",
                    },
                },
                {
                    "status": status.HTTP_200_OK,
                    "route": "/auth",
                    "data": {
                        "phone": "+70000000003",
                        "code": "0000",
                    },
                }
            ]
        ),
    )
    async def test_auth_request(self, client, prepare_users, actions):

        headers = {"Content-Type": "application/json"}
        for act in actions:
            response = await client.post(url=self.get_url() + act["route"], json=act["data"], headers=headers)
            assert response.status_code == act["status"]

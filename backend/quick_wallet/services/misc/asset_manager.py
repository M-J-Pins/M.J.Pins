import json
from typing import ByteString

import requests
from singleton_decorator import singleton

from quick_wallet.config import get_settings


@singleton
class AssetManager:
    base_url = "https://api.imgbb.com/1/upload"

    def upload_image(self, image: ByteString) -> str:
        response = requests.post(
            self.base_url + f"?key={get_settings().IMGBB_KEY}", files={"image": image}
        )
        url = json.loads(response.content.decode()).get("data").get("url")
        return url

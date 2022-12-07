import json
from typing import ByteString

import cv2
import numpy as np
import requests
from fastapi import UploadFile
from PIL import Image

from quick_wallet.config import get_settings
from quick_wallet.utils import random_word


class AssetManager:
    base_url = "https://api.imgbb.com/1/upload"

    def upload_image(self, image: ByteString) -> str:
        response = requests.post(
            self.base_url + f"?key={get_settings().IMGBB_KEY}", files={"image": image}
        )
        url = json.loads(response.content.decode()).get("data").get("url")
        return url

    @staticmethod
    def save_image(
        image: ByteString, filename: str = random_word(10), gray_duplicate: bool = False
    ) -> str:
        path_to_file = f"assets/{filename}.jpg"
        with open(path_to_file, "wb") as file:
            file.write(image)

        if gray_duplicate:
            gray_image = cv2.imread(path_to_file, cv2.IMREAD_GRAYSCALE)
            cv2.imwrite(f"assets/{filename}_GRAY.jpg", gray_image)

        return filename

    @staticmethod
    def get_gray_pil_img(filename: str) -> Image:
        return Image.open(f"assets/{filename}_GRAY.jpg")

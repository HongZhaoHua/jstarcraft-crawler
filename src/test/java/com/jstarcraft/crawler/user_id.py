# -*- coding: utf-8 -*-
# @Time : 2022/11/15 22:37
# @Author : xiaoer
# @Site : 
# @File : user_id.py
# @Desc:
# @Software: PyCharm
import requests

headers = {
    "user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36"
}
url = "https://api.meiwulist.com/api_service/api/v1/user/getNewUserId"
response = requests.get(url, headers=headers)

print(response.text)
print(response)

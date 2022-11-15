# -*- coding: utf-8 -*-
# @Time : 2022/11/15 21:08
# @Author : xiaoer
# @Site : 
# @File : meiwu_spider.py
# @Desc: 美物清单app
# @Software: PyCharm
import requests


def get_rank_two_data(page):
    """
    榜单2小时
    :param page: 页数
    :return:
    """
    headers = {
        # 'Host': 'api.meiwulist.com',
        'Accept': '*/*',
        'Connection': 'keep-alive',
        # 'timestamp': '1668511831295',
        'User-Agent': 'SQMotherPocket/3.1.91 (iPhone; iOS 16.1; Scale/3.00)',
        'Accept-Language': 'zh-Hans-CN;q=1',
    }
    data = {
        'cid': '0',
        'pageNo': page,
        'rankType': '1',  # 榜单类型
    }
    response = requests.post('https://api.meiwulist.com/api_service/api/v1/meiwu/listDtkCategoryGoods', headers=headers,
                             data=data)
    print(response.text)


def get_rank_data_day(page):
    """
    榜单全天
    :param page:页数
    :return:
    """
    headers = {
        'Host': 'api.meiwulist.com',
        'Accept': '*/*',
        'Connection': 'keep-alive',
        'timestamp': '1668511984670',
        'User-Agent': 'SQMotherPocket/3.1.91 (iPhone; iOS 16.1; Scale/3.00)',
        'Accept-Language': 'zh-Hans-CN;q=1',
    }
    data = {
        'cid': '0',
        'pageNo': page,
        'rankType': '2',  # 榜单类型
    }
    response = requests.post('https://api.meiwulist.com/api_service/api/v1/meiwu/listDtkCategoryGoods', headers=headers,
                             data=data)
    print(response.text)


def get_hot_sale_data(page):
    """
    热销
    :param page:页数
    :return:
    """
    headers = {
        'Host': 'api.meiwulist.com',
        'Accept': '*/*',
        'Connection': 'keep-alive',
        'timestamp': '1668512232254',
        'User-Agent': 'SQMotherPocket/3.1.91 (iPhone; iOS 16.1; Scale/3.00)',
        'Accept-Language': 'zh-Hans-CN;q=1',
    }

    data = {
        'category': '美妆',
        'pageNo': page,
        'userId': '23810943',  # 用户id
    }

    response = requests.post('https://api.meiwulist.com/api_service/api/v1/content/listYangmaoBaoliaoV2',
                             headers=headers, data=data)
    print(response.text)


def get_like_data(page):
    """
    猜你喜欢
    :param page:页数
    :return:
    """
    headers = {
        'Host': 'api.meiwulist.com',
        'Accept': '*/*',
        'Connection': 'keep-alive',
        'timestamp': '1668512446853',
        'User-Agent': 'SQMotherPocket/3.1.91 (iPhone; iOS 16.1; Scale/3.00)',
        'Accept-Language': 'zh-Hans-CN;q=1',
    }
    data = {
        'pageNo': page,
        'pindaoId': '11',
    }
    response = requests.post('https://api.meiwulist.com/api_service/api/v1/meiwu/listPindaoGoods', headers=headers,
                             data=data)
    print(response.text)


def get_free_shipping_data(page):
    """
    包邮
    :param page:页数
    :return:
    """
    headers = {
        'Host': 'api.meiwulist.com',
        'Accept': '*/*',
        'Connection': 'keep-alive',
        'timestamp': '1668512566877',
        'User-Agent': 'SQMotherPocket/3.1.91 (iPhone; iOS 16.1; Scale/3.00)',
        'Accept-Language': 'zh-Hans-CN;q=1',
    }

    data = {
        'pageNo': page,
        'userId': '23810214',  # 用户id
    }

    response = requests.post('https://api.meiwulist.com/api_service/api/v1/content/list99Goods', headers=headers,
                             data=data)
    print(response.text)


def run():
    # get_rank_two_data(page=1)
    # get_rank_data_day(page=1)
    get_hot_sale_data(page=1)
    # get_like_data(page=1)
    # get_free_shipping_data(page=1)


if __name__ == '__main__':
    run()

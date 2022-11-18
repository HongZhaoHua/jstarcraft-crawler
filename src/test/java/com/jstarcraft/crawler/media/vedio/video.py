# -*- coding: utf-8 -*-
# @Time : 2022/11/18 9:36
# @Author : xiao_er
# @Desc:
# @File : video.py
import json
import os
import requests
import re
from tqdm import tqdm


def down_video(url, path):
    """
    下载视频
    @param url:
    @param path:
    @return:
    """
    headers = {
        'referer': 'https://www.bilibili.com/',
        'user-agent': 'Mozilla/5.0(WindowsNT10.0;Win64;x64)AppleWebKit/537.36(KHTML,likeGecko)Chrome/103.0.0.0Safari/537.36'
    }
    if "/" in path:
        mkdir_path = "/".join(path.split("/")[:-1])
        if (not os.path.exists(mkdir_path)):
            os.makedirs(mkdir_path)
    try:
        resp = requests.get(url, headers=headers, stream=True, verify=False)
        file_size = int(resp.headers['Content-Length'])
    except:
        raise Exception('下载错误！！！')
    pbar = tqdm(total=file_size, initial=0, unit='B', unit_scale=True, leave=True)
    with open(path, 'wb') as f:
        for chunk in resp.iter_content(chunk_size=1024):
            if chunk:
                f.write(chunk)
                pbar.update(1024)  # 更新进度条
    pbar.close()
    return file_size


def download_all_video(bvid, page):
    """
    遍历所有视频
    @param bvid:
    @param page:
    @return:
    """

    # for page in range(int(total_page)):
    url = 'https://www.bilibili.com/video/{bvid}/?p={page}'.format(bvid=bvid, page=page + 1)
    resp = requests.get(url, headers=headers, verify=False)
    play_info_item = json.loads(re.findall('window.__playinfo__=(.*?)</script>', resp.text)[0])
    INITIAL_STATE = json.loads(re.findall('window.__INITIAL_STATE__=(.*?);\(function\(\)', resp.text)[0])
    video_title = INITIAL_STATE['videoData']['title']
    video_part = INITIAL_STATE['videoData']['pages'][page]['part']
    duration = INITIAL_STATE['videoData']['pages'][page]['duration']
    # 时长
    time_long = f"{int(duration / 60)}：{duration % 60}"
    video_url = play_info_item['data']['dash']['video'][0]['baseUrl']
    audio_url = play_info_item['data']['dash']['audio'][0]['baseUrl']
    base_path = os.getcwd().replace('\\', "/")
    video_path = f"{base_path}/{video_title}/{video_part}({time_long})._video.mp4"
    audio_path = f"{base_path}/{video_title}/{video_part}._audio.mp3"

    print(f'开始下载 视频  路径:{video_path}')
    down_video(video_url, path=video_path)
    print(f'开始下载 音频 路径：{audio_path}')
    down_video(audio_url, path=audio_path)
    merge_video(video_path, audio_path)


def merge_video(video_pagh, audio_path):
    """
    将视频和声音合并
    @param video_pagh:
    @param audio_path:
    @return:
    """
    os.system(
        'ffmpeg -n -loglevel quiet -i \"{video}\" -i \"{audio}\" -c copy \"{video_out}\"'.format(video=video_pagh,
                                                                                                     audio=audio_path,
                                                                                                     video_out=video_pagh.replace(
                                                                                                         '._video.mp4',
                                                                                                         ".mp4")))
    os.remove(video_pagh)
    os.remove(audio_path)
    print(video_pagh.replace('._video.mp4', ".mp4"), "下载并合并完成")


def main():
    # 视频bid
    bvid = "BV1Ut4y1N7Hk"  # BV1nD4y1C7Yx  BV1Ut4y1N7Hk
    video_base_url = 'https://www.bilibili.com/video/' + bvid
    resp = requests.get(url=video_base_url, headers=headers, verify=False)
    try:
        total_page = re.findall('>\(\d/(\d+)\)</span', resp.text)[0]
    except:
        total_page = 0
    for page in range(0, int(total_page) + 1):
        download_all_video(bvid, page)


if __name__ == '__main__':
    headers = {
        'authority': 'www.bilibili.com',
        'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9',
        'referer': 'www.bilibili.com',
        'user-agent': 'Mozilla/5.0(WindowsNT10.0;Win64;x64)AppleWebKit/537.36(KHTML,likeGecko)Chrome/103.0.0.0Safari/537.36'
    }
    main()

import request from '@/utils/request'

export function getTagList(params) {
  return request({
    url: '/crm/tag/list',
    method: 'get',
    params
  })
}

export function getTag(id) {
  return request({
    url: `/crm/tag/${id}`,
    method: 'get'
  })
}

export function addTag(data) {
  return request({
    url: '/crm/tag',
    method: 'post',
    data
  })
}

export function updateTag(data) {
  return request({
    url: '/crm/tag',
    method: 'put',
    data
  })
}

export function deleteTag(id) {
  return request({
    url: `/crm/tag/${id}`,
    method: 'delete'
  })
}

import request from '@/utils/request'

export function getClientList(params) {
  return request({
    url: '/crm/client/list',
    method: 'get',
    params
  })
}

export function getClient(id) {
  return request({
    url: `/crm/client/${id}`,
    method: 'get'
  })
}

export function addClient(data) {
  return request({
    url: '/crm/client',
    method: 'post',
    data
  })
}

export function updateClient(data) {
  return request({
    url: '/crm/client',
    method: 'put',
    data
  })
}

export function deleteClient(id) {
  return request({
    url: `/crm/client/${id}`,
    method: 'delete'
  })
}

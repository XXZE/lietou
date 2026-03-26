import request from '@/utils/request'

export function getJobList(params) {
  return request({
    url: '/crm/job/list',
    method: 'get',
    params
  })
}

export function getJob(id) {
  return request({
    url: `/crm/job/${id}`,
    method: 'get'
  })
}

export function addJob(data) {
  return request({
    url: '/crm/job',
    method: 'post',
    data
  })
}

export function updateJob(data) {
  return request({
    url: '/crm/job',
    method: 'put',
    data
  })
}

export function deleteJob(id) {
  return request({
    url: `/crm/job/${id}`,
    method: 'delete'
  })
}

export function getJobBoard(params) {
  return request({
    url: '/crm/job/board',
    method: 'get',
    params
  })
}

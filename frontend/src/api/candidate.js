import request from '@/utils/request'

export function getCandidateList(params) {
  return request({
    url: '/crm/candidate/list',
    method: 'get',
    params
  })
}

export function getCandidate(id) {
  return request({
    url: `/crm/candidate/${id}`,
    method: 'get'
  })
}

export function addCandidate(data) {
  return request({
    url: '/crm/candidate',
    method: 'post',
    data
  })
}

export function updateCandidate(data) {
  return request({
    url: '/crm/candidate',
    method: 'put',
    data
  })
}

export function deleteCandidate(id) {
  return request({
    url: `/crm/candidate/${id}`,
    method: 'delete'
  })
}

export function checkDuplicate(params) {
  return request({
    url: '/crm/candidate/checkDuplicate',
    method: 'get',
    params
  })
}

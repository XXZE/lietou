import request from '@/utils/request'

export function getRecommendationList(params) {
  return request({
    url: '/crm/recommendation/list',
    method: 'get',
    params
  })
}

export function getRecommendation(id) {
  return request({
    url: `/crm/recommendation/${id}`,
    method: 'get'
  })
}

export function addRecommendation(data) {
  return request({
    url: '/crm/recommendation',
    method: 'post',
    data
  })
}

export function updateRecommendation(data) {
  return request({
    url: '/crm/recommendation',
    method: 'put',
    data
  })
}

export function deleteRecommendation(id) {
  return request({
    url: `/crm/recommendation/${id}`,
    method: 'delete'
  })
}

export function updateRecommendationStatus(id, status, userId) {
  return request({
    url: `/crm/recommendation/status/${id}`,
    method: 'put',
    params: { status, userId }
  })
}

export function getRecommendationStatistics(userId) {
  return request({
    url: '/crm/recommendation/statistics',
    method: 'get',
    params: { userId }
  })
}

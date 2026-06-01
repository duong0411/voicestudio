export enum ResultEnum {
  Success = 0,
  Error = 400,
  Unauthorized = 401,
  Forbidden = 403,
  NotFound = 404,
  MethodNotAllowed = 405,
  RequestTimeout = 408,
  InternalServerError = 500,
  NotImplemented = 501,
  BadGateway = 502,
  ServiceUnavailable = 503,
  GatewayTimeout = 504,
  HttpVersionNotSupported = 505,
  MixedContent = 600,
}
export enum ContentTypeEnum {
  JSON = 'application/json;charset=UTF-8',
  FORM_URLENCODED = 'application/x-www-form-urlencoded;charset=UTF-8',
  FORM_DATA = 'multipart/form-data;charset=UTF-8',
}

export function ShowMessage(status: number | string): string {
  let message: string
  switch (status) {
    case 400:
      message = '(400)'
      break
    case 401:
      message = '，(401)'
      break
    case 403:
      message = '(403)'
      break
    case 404:
      message = '(404)'
      break
    case 408:
      message = '(408)'
      break
    case 500:
      message = '(500)'
      break
    case 501:
      message = '(501)'
      break
    case 502:
      message = '(502)'
      break
    case 503:
      message = '(503)'
      break
    case 504:
      message = '(504)'
      break
    case 505:
      message = 'HTTP(505)'
      break
    case 600:
      message = '(600)'
      break
    default:
      message = `(${status})!`
  }
  return `${message}，！`
}

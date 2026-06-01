import { getServiceUrl } from '../api';
import RequestService from '../httpRequest';


export default {

    getFileList(params, callback) {
        const queryParams = new URLSearchParams({
            page: params.page,
            pageSize: params.pageSize
        }).toString();

        RequestService.sendRequest()
            .url(`${getServiceUrl()}/correct-word/file/list?${queryParams}`)
            .method('GET')
            .success((res) => {
                RequestService.clearRequestTime()
                callback(res)
            })
            .networkFail((err) => {
                console.error(':', err)
                RequestService.reAjaxFun(() => {
                    this.getFileList(params, callback)
                })
            }).send()
    },


    selectAll(callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/correct-word/file/select`)
            .method('GET')
            .success((res) => {
                RequestService.clearRequestTime()
                callback(res)
            })
            .networkFail((err) => {
                console.error(':', err)
                RequestService.reAjaxFun(() => {
                    this.selectAll(callback)
                })
            }).send()
    },


    downloadFile(id, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/correct-word/file/download/${id}`)
            .method('GET')
            .success((res) => {
                RequestService.clearRequestTime()
                callback(res)
            })
            .fail((err) => {
              RequestService.clearRequestTime()
              callback(err)
            }).send()
    },


    addFile(data, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/correct-word/file`)
            .method('POST')
            .data(data)
            .success((res) => {
                RequestService.clearRequestTime()
                callback(res)
            })
            .fail((err) => {
              RequestService.clearRequestTime()
              callback(err)
            }).send()
    },


    updateFile(data, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/correct-word/file/${data.id}`)
            .method('PUT')
            .data({
                fileName: data.fileName,
                content: data.content
            })
            .success((res) => {
                RequestService.clearRequestTime()
                callback(res)
            })
            .fail((err) => {
              RequestService.clearRequestTime()
              callback(err)
            }).send()
    },


    deleteFile(id, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/correct-word/file/${id}`)
            .method('DELETE')
            .success((res) => {
                RequestService.clearRequestTime()
                callback(res)
            })
            .networkFail((err) => {
                console.error(':', err)
                RequestService.reAjaxFun(() => {
                    this.deleteFile(id, callback)
                })
            }).send()
    },


    batchDeleteFile(ids, callback) {
        RequestService.sendRequest()
            .url(`${getServiceUrl()}/correct-word/file/batch-delete`)
            .method('POST')
            .data(ids)
            .success((res) => {
                RequestService.clearRequestTime()
                callback(res)
            })
            .networkFail((err) => {
                console.error(':', err)
                RequestService.reAjaxFun(() => {
                    this.batchDeleteFile(ids, callback)
                })
            }).send()
    }
}

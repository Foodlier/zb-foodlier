/* eslint-disable react/require-default-props */
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import * as S from '../../styles/refrigerator/QuotationItem.styled'
import ModalWithTwoButton from '../ui/ModalWithTwoButton'
import axiosInstance from '../../utils/FetchCall'
import { Quotation, RequestForm } from '../../constants/Interfaces'

interface RequestItemProps {
  item: RequestForm | Quotation
  postRequest?: (id: number) => void
  isVisibleSelectButton: boolean
  refresh: () => void
}

const RequestItem = ({
  item,
  postRequest,
  isVisibleSelectButton,
  refresh,
}: RequestItemProps) => {
  const navigate = useNavigate()
  const [isDeleteModal, setIsDeleteModal] = useState(false)

  const editRequest = () => {
    if (!('requestFormId' in item)) return

    navigate('/refrigerator/request/write', {
      state: { requestFormId: item.requestFormId },
    })
  }

  const deleteRequest = async () => {
    if (!('requestFormId' in item)) return

    const res = await axiosInstance.delete(
      `/api/refrigerator/${item.requestFormId}`
    )
    console.log(res)
    setIsDeleteModal(false)
    refresh()
  }

  if (!('requestFormId' in item)) return null

  return (
    <>
      <S.El key={item.requestFormId}>
        <div>
          <S.ElTitle>{item.title}</S.ElTitle>
          <S.ElContents>{item.content}</S.ElContents>
        </div>
        <S.ButtonContainer>
          {isVisibleSelectButton && (
            <S.RequestButton
              type="button"
              onClick={() => {
                if (postRequest) {
                  postRequest(item.requestFormId)
                }
              }}
            >
              요청 보내기
            </S.RequestButton>
          )}

          <S.MoreButtonContainer>
            <S.MoreButton type="button" onClick={editRequest}>
              수정
            </S.MoreButton>
            <S.MoreButton type="button" onClick={() => setIsDeleteModal(true)}>
              삭제
            </S.MoreButton>
          </S.MoreButtonContainer>
        </S.ButtonContainer>
      </S.El>
      {isDeleteModal && (
        <ModalWithTwoButton
          content="해당 요청서를 삭제하시겠습니까?"
          setIsModalFalse={() => setIsDeleteModal(false)}
          modalEvent={deleteRequest}
        />
      )}
    </>
  )
}

export default RequestItem

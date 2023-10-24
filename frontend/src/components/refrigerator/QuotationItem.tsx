import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import * as S from '../../styles/refrigerator/QuotationItem.styled'
import axiosInstance from '../../utils/FetchCall'
import ModalWithTwoButton from '../ui/ModalWithTwoButton'
import { RequestForm } from '../../constants/Interfaces'

// 제네릭 타입으로 구현
interface Quotation {
  content: string
  difficulty: string
  expectedTime: number
  quotationId: number
  title: string
}

interface QuotationItemProps {
  item: Quotation | RequestForm
  onClickSelectButton: (id: number) => void
  refresh: () => void
}

const QuotationItem = ({
  item,
  onClickSelectButton,
  refresh,
}: QuotationItemProps) => {
  const navigate = useNavigate()
  const [isDeleteModal, setIsDeleteModal] = useState(false)

  const deleteQuotation = async () => {
    if (!('quotationId' in item)) return
    const res = await axiosInstance.delete(`/api/quotation/${item.quotationId}`)
    console.log(res)
    setIsDeleteModal(false)
    refresh()
  }

  if (!('quotationId' in item)) return null

  return (
    <S.El key={item.quotationId}>
      <div>
        <S.ElTitle>{item.title}</S.ElTitle>
        <S.ElContents>{item.content}</S.ElContents>
      </div>
      <S.ButtonContainer>
        <S.RequestButton
          type="button"
          onClick={() => onClickSelectButton(item.quotationId)}
        >
          선택하기
        </S.RequestButton>
        <S.MoreButtonContainer>
          <S.MoreButton
            type="button"
            onClick={() =>
              navigate('/refrigerator/quotation/write', {
                state: { quotationId: item.quotationId },
              })
            }
          >
            수정
          </S.MoreButton>
          <S.MoreButton type="button" onClick={() => setIsDeleteModal(true)}>
            삭제
          </S.MoreButton>
        </S.MoreButtonContainer>
      </S.ButtonContainer>
      {isDeleteModal && (
        <ModalWithTwoButton
          content="해당 견적서를 삭제하시겠습니까?"
          setIsModalFalse={() => setIsDeleteModal(false)}
          modalEvent={deleteQuotation}
        />
      )}
    </S.El>
  )
}

export default QuotationItem

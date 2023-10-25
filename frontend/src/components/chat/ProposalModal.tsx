import { useState } from 'react'
import * as S from '../../styles/chat/ProposalModal.styled'
import axiosInstance from '../../utils/FetchCall'

interface ProposalModalProps {
  setIsProposalModalOpen: React.Dispatch<React.SetStateAction<boolean>>
  sendSuggestion: (price: number) => void
  roomId: number | undefined
}

const ProposalModal = ({
  setIsProposalModalOpen,
  sendSuggestion,
  roomId,
}: ProposalModalProps) => {
  const [price, setPrice] = useState(0)
  const [alert, setAlert] = useState(false)

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target
    // value의 값이 숫자가 아닐경우 빈문자열로 replace 해버림.
    const onlyNumber = Number(value.replace(/[^0-9]/g, ''))
    setPrice(onlyNumber)
  }

  const body = {
    suggestedPrice: price,
  }

  const suggest = async () => {
    try {
      const res = await axiosInstance.post(`/api/point/suggest/${roomId}`, body)
      sendSuggestion(price)
      setIsProposalModalOpen(false)
      console.log('제안 요청에 대한 반응 : ', res)
    } catch (error) {
      console.log('제안 요청에 대한 에러 : ', error)
      setAlert(true)
    }
  }

  return (
    <S.ModalBackdrop onClick={() => setIsProposalModalOpen(false)}>
      <S.Container onClick={e => e.stopPropagation()}>
        <S.Title>제안 금액을 입력해주세요.</S.Title>
        <S.WrapInput>
          <S.Input type="text" value={price} onChange={onChange} />원
        </S.WrapInput>
        {alert && <S.Alert>이미 제안 됐거나 종료되었습니다.</S.Alert>}
        <S.Button
          onClick={() => {
            suggest()
          }}
        >
          제안하기
        </S.Button>
      </S.Container>
    </S.ModalBackdrop>
  )
}

export default ProposalModal

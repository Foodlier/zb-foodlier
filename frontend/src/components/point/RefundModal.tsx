import React, { useState } from 'react'
import axiosInstance from '../../utils/FetchCall'
import * as S from '../../styles/point/RefundModal.styled'

interface ChargeModalProps {
  setIsRefundModal: React.Dispatch<React.SetStateAction<boolean>>
  paymentKey: string
}

const RefundModal: React.FC<ChargeModalProps> = ({
  setIsRefundModal,
  paymentKey,
}) => {
  const [cancelReason, setCancelReason] = useState('사유 없음')
  const [buttonDesc, setButtonDesc] = useState('환불하기')
  const [isLoading, setIsLoading] = useState(false)
  const [isSuccess, setIsSuccess] = useState(false)
  const [isFail, setIsFail] = useState(false)

  const refund = async (paymentKEY: string) => {
    setButtonDesc('처리중')
    setIsLoading(true)
    try {
      const res = await axiosInstance.post(
        `/api/point/cancel?paymentKey=${paymentKEY}&cancelReason=${cancelReason}`
      )
      if (res.status === 200) {
        setIsSuccess(true)
        setTimeout(() => {
          setIsRefundModal(false)
          setIsSuccess(false)
          setCancelReason('')
        }, 2000)
      }
    } catch (error) {
      setIsFail(true)
      setTimeout(() => {
        setIsRefundModal(false)
        setIsFail(false)
      }, 2000)
    }
    setButtonDesc('환불하기')
    setIsLoading(false)
  }

  return (
    <S.ModalBackdrop onClick={() => setIsRefundModal(false)}>
      <S.Container onClick={e => e.stopPropagation()}>
        {isFail && <S.Title>오류가 발생하여 환불이 실패했습니다.</S.Title>}
        {isSuccess && <S.Title>환불처리가 완료되었습니다.</S.Title>}
        {!isFail && !isSuccess && (
          <>
            <S.Title>환불 사유를 입력해주세요.</S.Title>
            <S.WrapInput>
              <S.Input
                type="text"
                onChange={e => setCancelReason(e.target.value)}
                placeholder="사유 없음"
              />
            </S.WrapInput>
            <S.Button disabled={isLoading} onClick={() => refund(paymentKey)}>
              {buttonDesc}
            </S.Button>
          </>
        )}
      </S.Container>
    </S.ModalBackdrop>
  )
}

export default RefundModal

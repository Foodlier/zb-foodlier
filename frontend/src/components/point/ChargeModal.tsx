import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import axiosInstance from '../../utils/FetchCall'
import * as S from '../../styles/point/ChargeModal.styled'

interface ChargeModalProps {
  setIsChargeModal: React.Dispatch<React.SetStateAction<boolean>>
}

const ChargeModal: React.FC<ChargeModalProps> = ({ setIsChargeModal }) => {
  const navigate = useNavigate()
  const [price, setPrice] = useState(1000)
  const [alert, setAlert] = useState(false)

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target
    // value의 값이 숫자가 아닐경우 빈문자열로 replace 해버림.
    const onlyNumber = Number(value.replace(/[^0-9]/g, ''))
    setPrice(onlyNumber)
  }
  // 최소주문 설정

  const body = {
    payType: 'CARD',
    amount: price,
    orderName: 'CHARGE',
  }

  const goToCharge = async () => {
    try {
      const res = await axiosInstance.post('/api/point/charge', body)
      if (res.status === 200) {
        navigate('/point', { state: res.data })
      }
    } catch (error) {
      setAlert(true)
    }
  }
  return (
    <S.ModalBackdrop onClick={() => setIsChargeModal(false)}>
      <S.Container onClick={e => e.stopPropagation()}>
        <S.Title>충전 금액을 입력해주세요.</S.Title>
        <S.WrapInput>
          <S.Input type="text" value={price} onChange={onChange} />원
        </S.WrapInput>
        {alert && <S.Alert>1000원 이상부터 충전 가능합니다.</S.Alert>}
        <S.Button onClick={goToCharge}>충전하기</S.Button>
      </S.Container>
    </S.ModalBackdrop>
  )
}

export default ChargeModal

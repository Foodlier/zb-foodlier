import React, { useEffect, useState } from 'react'
import axiosInstance from '../../utils/FetchCall'
import * as S from '../../styles/point/DealModal.styled'

interface Price {
  price: number
  setIsDealModalOpen: React.Dispatch<React.SetStateAction<boolean>>
}

const DealModal: React.FC<Price> = ({ price, setIsDealModalOpen }) => {
  const [point, setPoint] = useState(0)
  const [isNeedMore, setIsNeedMore] = useState(false)

  const getPoint = async () => {
    try {
      const res = await axiosInstance.get('/profile/private')
      console.log('잔여  포인트', res.data.point)
      setPoint(res.data.point)
    } catch (error) {
      console.log(error)
    }
  }

  const judgeNeedMore = () => {
    if (point >= price) {
      setIsNeedMore(false)
    } else {
      setIsNeedMore(true)
    }
  }

  useEffect(() => {
    getPoint()
  }, [])

  useEffect(() => {
    judgeNeedMore()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [point])

  let need = false
  if (point < price) {
    need = true
  }

  return (
    <>
      <S.GreyBackground />
      <S.SelectMoadl>
        <S.ChargingInfo>
          <S.ShowCurrentInfo>
            {need ? (
              <>
                <S.MainInfo>포인트가 부족합니다. 충전하시겠습니까?</S.MainInfo>
                <S.MoreInfo>
                  부족한 포인트 : <S.MoreMoney>{price - point}</S.MoreMoney> 원
                </S.MoreInfo>
              </>
            ) : (
              <>
                <S.MainInfo>거래 진행하시겠습니까?</S.MainInfo>
                <S.MoreInfo>상대 제안 금액 : {price}원</S.MoreInfo>
              </>
            )}

            <S.PointInfo>잔여 포인트 : {point}원</S.PointInfo>
          </S.ShowCurrentInfo>
        </S.ChargingInfo>
        <S.ButtonList>
          <S.RejectButton
            type="button"
            onClick={() => setIsDealModalOpen(false)}
          >
            취소
          </S.RejectButton>
          {need ? (
            <S.AcceptButton type="button">충전</S.AcceptButton>
          ) : (
            <S.AcceptButton type="button">수락</S.AcceptButton>
          )}
        </S.ButtonList>
      </S.SelectMoadl>
    </>
  )
}

export default DealModal

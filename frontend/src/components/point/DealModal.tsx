import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import axiosInstance from '../../utils/FetchCall'
import * as S from '../../styles/point/DealModal.styled'
import { RoomInfoInterface } from '../chat/RoomListItem'

interface Price {
  price: number
  roomId: number | undefined
  setIsDealModalOpen: React.Dispatch<React.SetStateAction<boolean>>
  sendMessage: (answer: string) => void
}

const DealModal: React.FC<Price> = ({
  price,
  roomId,
  setIsDealModalOpen,
  sendMessage,
}) => {
  const navigate = useNavigate()
  const [point, setPoint] = useState(0)
  const [isLoading, setIsLoading] = useState(false)
  const [isSuggested, setIsSuggested] = useState(false)
  // const [isNeedMore, setIsNeedMore] = useState(false)

  // 방 정보 가져오기
  const getRoomInfo = async () => {
    try {
      const res = await axiosInstance.get('/api/dm/room/0/10')
      if (res.status === 200) {
        const newRoomInfo = res.data.content.find(
          (item: RoomInfoInterface) => item.roomId === roomId
        )
        setIsSuggested(newRoomInfo.isSuggested)
      }
    } catch (error) {
      console.log(error)
    }
  }

  const getPoint = async () => {
    try {
      const res = await axiosInstance.get('/api/profile/private')
      setPoint(res.data.point)
      setIsLoading(true)
    } catch (error) {
      console.log(error)
    }
  }

  // const judgeNeedMore = () => {
  //   if (point >= price) {
  //     setIsNeedMore(false)
  //   } else {
  //     setIsNeedMore(true)
  //   }
  // }

  useEffect(() => {
    getRoomInfo()
    getPoint()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  // useEffect(() => {
  //   judgeNeedMore()
  //   // eslint-disable-next-line react-hooks/exhaustive-deps
  // }, [point])

  let need = false
  if (point < price) {
    need = true
  }

  const body = {
    payType: 'CARD',
    amount: `${price - point}`,
    orderName: 'CHARGE',
  }

  const goToCharge = async () => {
    try {
      const res = await axiosInstance.post('/api/point/charge', body)
      console.log(res.data)
      navigate('/point', { state: res.data })
    } catch (error) {
      console.log(error)
    }
  }

  const approve = async () => {
    try {
      const res = await axiosInstance.patch(
        `/api/point/suggest/approve/${roomId}`
      )
      if (res.status === 200) {
        sendMessage('approve')
        setIsDealModalOpen(false)
      }
    } catch (error) {
      console.log(error)
    }
  }

  return (
    <>
      <S.GreyBackground />
      {isLoading && (
        <S.SelectMoadl>
          <S.ChargingInfo>
            {isSuggested && (
              <S.ShowCurrentInfo>
                {need ? (
                  <>
                    <S.MainInfo>
                      포인트가 부족합니다. 충전하시겠습니까?
                    </S.MainInfo>
                    <S.MoreInfo>
                      부족한 포인트 : <S.MoreMoney>{price - point}</S.MoreMoney>{' '}
                      원
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
            )}
            {!isSuggested && (
              <S.ShowCurrentInfo>
                <S.MainInfo>만료된 제안입니다.</S.MainInfo>
              </S.ShowCurrentInfo>
            )}
          </S.ChargingInfo>
          <S.ButtonList>
            {isSuggested && (
              <>
                {' '}
                <S.RejectButton
                  type="button"
                  onClick={() => setIsDealModalOpen(false)}
                >
                  취소
                </S.RejectButton>
                {need ? (
                  <S.AcceptButton type="button" onClick={goToCharge}>
                    충전
                  </S.AcceptButton>
                ) : (
                  <S.AcceptButton
                    type="button"
                    onClick={() => {
                      approve()
                    }}
                  >
                    수락
                  </S.AcceptButton>
                )}
              </>
            )}
            {!isSuggested && (
              <S.closeButton
                type="button"
                onClick={() => setIsDealModalOpen(false)}
              >
                닫기
              </S.closeButton>
            )}
          </S.ButtonList>
        </S.SelectMoadl>
      )}
    </>
  )
}

export default DealModal

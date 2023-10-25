import React, { useState, useEffect } from 'react'
import axiosInstance from '../../utils/FetchCall'
import * as S from '../../styles/point/DealModal.styled'
import { RoomInfoInterface } from '../chat/RoomListItem'

interface Price {
  roomId: number | undefined
  setIsRejectModalOpen: React.Dispatch<React.SetStateAction<boolean>>
  sendMessage: (answer: string) => void
}

const RejectModal: React.FC<Price> = ({
  roomId,
  setIsRejectModalOpen,
  sendMessage,
}) => {
  const [isSuggested, setIsSuggested] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
  // 방 정보 가져오기
  const getRoomInfo = async () => {
    try {
      const res = await axiosInstance.get('/api/dm/room/0/10')
      if (res.status === 200) {
        const newRoomInfo = res.data.content.find(
          (item: RoomInfoInterface) => item.roomId === roomId
        )
        setIsSuggested(newRoomInfo.isSuggested)
        setIsLoading(true)
      }
    } catch (error) {
      console.log(error)
    }
  }

  const reject = async () => {
    try {
      const res = await axiosInstance.patch(
        `/api/point/suggest/reject/${roomId}`
      )
      if (res.status === 200) {
        console.log(res.data)
        setIsRejectModalOpen(false)
        sendMessage('reject')
      }
    } catch (error) {
      console.log(error)
    }
  }

  useEffect(() => {
    getRoomInfo()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  return (
    <>
      <S.GreyBackground />
      {isLoading && (
        <S.SelectMoadl>
          <S.ChargingInfo>
            <S.ShowCurrentInfo>
              {isSuggested ? (
                <S.MainInfo>제안을 거절하시겠습니까?</S.MainInfo>
              ) : (
                <S.MainInfo>만료된 제안입니다.</S.MainInfo>
              )}
            </S.ShowCurrentInfo>
          </S.ChargingInfo>
          <S.ButtonList>
            {isSuggested ? (
              <>
                <S.RejectButton
                  type="button"
                  onClick={() => setIsRejectModalOpen(false)}
                >
                  취소
                </S.RejectButton>
                <S.AcceptButton type="button" onClick={reject}>
                  거절
                </S.AcceptButton>
              </>
            ) : (
              <S.closeButton
                type="button"
                onClick={() => setIsRejectModalOpen(false)}
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

export default RejectModal

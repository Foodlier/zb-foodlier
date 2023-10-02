import { useState } from 'react'
import { palette } from '../../constants/Styles'
import useIcon from '../../hooks/useIcon'
import * as S from '../../styles/chat/ChatRoom.styled'
import ProposalModal from './ProposalModal'
import { RoomInfoInterface } from './RoomListItem'
import ModalWithTwoButton from '../ui/ModalWithTwoButton'
import getTime from '../../utils/getTime'

const ChatRoom = ({
  roomInfo,
}: {
  roomInfo: RoomInfoInterface | undefined
}) => {
  const dummy = [
    {
      id: 0,
      isMe: false,
      message:
        '긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시',
      imageUrl: '',
      createdAt: '2023-09-21T09:00:00',
    },

    {
      id: 1,
      isMe: true,
      message: '메세지2',
      imageUrl: '',
      createdAt: '2023-09-21T09:00:00',
    },
    {
      id: 2,
      isMe: true,
      message: '메세지3',
      imageUrl: '',
      createdAt: '2023-09-21T09:00:00',
    },
  ]
  const { IcImgBoxLight } = useIcon()

  const [isProposalModalOpen, setIsProposalModalOpen] = useState(false)
  const [isExitModalOpen, setIsExitModalOpen] = useState(false)

  return (
    <S.Container>
      <S.ChattingHeader>
        <S.FlexAlignCenter>
          <S.ProfileImage src={roomInfo?.targetProfileImageUrl} $size={4} />
          <S.Nickname>{roomInfo?.targetNickName}</S.Nickname>
        </S.FlexAlignCenter>
        <S.FlexAlignCenter>
          <S.ProposalButton onClick={() => setIsProposalModalOpen(true)}>
            제안하기
          </S.ProposalButton>
          <S.ExitButton onClick={() => setIsExitModalOpen(true)}>
            채팅방 나가기
          </S.ExitButton>
        </S.FlexAlignCenter>
      </S.ChattingHeader>
      <S.ChattingMessage>
        {dummy.map(item => (
          <S.WrapMessage key={item.id} $isMe={item.isMe}>
            <S.Wrap>
              {!item.isMe && (
                <S.ProfileImage
                  src={roomInfo?.targetProfileImageUrl}
                  $size={3}
                />
              )}
              <S.Message $isMe={item.isMe}>{item.message}</S.Message>
            </S.Wrap>
            <S.MessageTime>{getTime(item.createdAt)}</S.MessageTime>
          </S.WrapMessage>
        ))}
        <S.WrapDate>2023.09.02</S.WrapDate>
      </S.ChattingMessage>
      <S.WrapInput>
        <IcImgBoxLight size={3.5} color={palette.textPrimary} />
        <S.Input />
        <S.Button>전송</S.Button>
      </S.WrapInput>
      {isProposalModalOpen && (
        <ProposalModal setIsProposalModalOpen={setIsProposalModalOpen} />
      )}
      {isExitModalOpen && (
        <ModalWithTwoButton
          content="채팅방을 나가시겠습니까?"
          setIsModalFalse={() => setIsExitModalOpen(false)}
          modalEvent={() => {
            console.log('채팅방 나가기')
          }}
        />
      )}
    </S.Container>
  )
}

export default ChatRoom

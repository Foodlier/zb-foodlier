import React from 'react'
import * as S from '../../styles/refrigerator/ListModal.styled'

interface ModalProps {
  setModalOpen: React.Dispatch<React.SetStateAction<boolean>>
  modalType: string
}

const ListModal: React.FC<ModalProps> = ({ setModalOpen, modalType }) => {
  const closeModal = () => {
    setModalOpen(false)
  }

  const modalOPtion = {
    type: '',
    button: '',
  }

  if (modalType === 'estimate') {
    modalOPtion.type = '견적서'
    modalOPtion.button = '선택하기'
  } else {
    modalOPtion.type = '요청서'
    modalOPtion.button = '요청하기'
  }

  const REQUEST_LIST_EXAMPLE = [
    {
      title: '남아버린 치킨을 되살릴 수 있을까요?',
      contents: '도와주세요, 집에 치킨이랑 마요네즈 정도만 있어요',
    },
    {
      title: '남아버린 치킨을 되살릴 수 있을까요?',
      contents: '높은 하늘을 다시 날 수 있도록 도와주세요..!',
    },
  ]

  return (
    <>
      <S.GreyBackground />
      <S.ModalScreen>
        <S.ModalTop>
          <span>나의 {modalOPtion.type} 목록</span>
          <button type="button" onClick={closeModal}>
            X
          </button>
        </S.ModalTop>
        <div>
          <S.ElContainer>
            {REQUEST_LIST_EXAMPLE.map(el => (
              // key 값 뭘로 해야하지?
              <S.El key={el.contents}>
                {/* <S.ElImg src="" alt="" /> */}
                <div>
                  <S.ElTitle>{el.title}</S.ElTitle>
                  <S.ElContents>{el.contents}</S.ElContents>
                </div>
                <S.ButtonContainer>
                  <S.RequestButton type="button">
                    {modalOPtion.button}
                  </S.RequestButton>
                  <S.MoreButtonContainer>
                    <S.MoreButton type="button">수정</S.MoreButton>
                    <S.MoreButton type="button">삭제</S.MoreButton>
                  </S.MoreButtonContainer>
                </S.ButtonContainer>
              </S.El>
            ))}
          </S.ElContainer>
        </div>
      </S.ModalScreen>
    </>
  )
}

export default ListModal

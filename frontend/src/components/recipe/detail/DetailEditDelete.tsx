import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import * as S from '../../../styles/recipe/detail/DetailEditDelete.styled'
import ModalWithTwoButton from '../../ui/ModalWithTwoButton'
import axiosInstance from '../../../utils/FetchCall'
import ModalWithoutButton from '../../ui/ModalWithoutButton'

const DetailEditDelete = ({ recipeId }: { recipeId: number }) => {
  const navigate = useNavigate()

  const [isDeleteModal, setIsDeleteModal] = useState(false)
  const [isCompleteModal, setIsCompleteModal] = useState(false)
  const [modalContent, setModalContent] = useState('')

  const deleteRecipe = async () => {
    try {
      const { status } = await axiosInstance.delete(`/api/recipe/${recipeId}`)
      if (status === 200) {
        setModalContent('삭제가 완료되었습니다.')
      }
    } catch (error) {
      console.log(error)
      setModalContent('게시물 삭제를 실패하였습니다.')
    }
    setIsDeleteModal(false)
    setIsCompleteModal(true)
    setTimeout(() => {
      setIsCompleteModal(false)
      navigate(-1)
    }, 1500)
  }

  return (
    <S.WrapButton>
      <S.Button
        onClick={() => {
          navigate('/recipe/write', {
            state: { recipeId },
          })
        }}
      >
        수정하기
      </S.Button>
      <S.Button onClick={() => setIsDeleteModal(true)}>삭제하기</S.Button>
      {isDeleteModal && (
        <ModalWithTwoButton
          content="게시물을 삭제하시겠습니까?"
          setIsModalFalse={() => {
            setIsDeleteModal(false)
          }}
          modalEvent={deleteRecipe}
        />
      )}
      {isCompleteModal && (
        <ModalWithoutButton
          content={modalContent}
          setIsModalFalse={() => setIsCompleteModal(false)}
        />
      )}
    </S.WrapButton>
  )
}
export default DetailEditDelete

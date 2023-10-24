import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import BottomNavigation from '../../components/BottomNavigation'
import Header from '../../components/Header'
import * as S from '../../styles/user/EditPasswordPage.styled'
import axiosInstance from '../../utils/FetchCall'
import {
  validatePassword,
  validatePasswordConfirm,
} from '../../utils/FormValidation'
import ModalWithoutButton from '../../components/ui/ModalWithoutButton'

const EditPasswordPage = () => {
  const navigate = useNavigate()
  const [isCompleteModal, setIsCompleteModal] = useState(false)
  const [modalContent, setModalContent] = useState('')
  const [passwordValue, setPasswordValue] = useState({
    currentPassword: '',
    newPassword: '',
  })
  const [passwordError, setPasswordError] = useState({
    newPassword: '',
    newPasswordCheck: '',
  })
  const [newPasswordCheck, setNewPasswordCheck] = useState('')

  const editPassword = async () => {
    if (
      passwordValue.currentPassword === '' ||
      passwordValue.newPassword === '' ||
      passwordError.newPassword !== '' ||
      passwordError.newPasswordCheck !== ''
    )
      return

    try {
      const res = await axiosInstance.put(
        '/api/profile/private/password',
        passwordValue
      )
      setIsCompleteModal(true)
      setModalContent('비밀번호 수정이 완료되었습니다.')
      setTimeout(() => {
        setIsCompleteModal(false)
        navigate(-1)
      }, 1500)
      console.log(res)
    } catch (err) {
      setIsCompleteModal(true)
      setModalContent('잘못 된 비밀번호입니다.')
      setTimeout(() => {
        setIsCompleteModal(false)
      }, 1500)
    }
  }

  return (
    <>
      <Header />
      <S.Container>
        <S.Title>비밀번호 수정</S.Title>
        <S.FlexWrap>
          <S.Label>현재 비밀번호</S.Label>
          <S.Wrap>
            <S.Input
              type="password"
              value={passwordValue.currentPassword}
              onChange={e =>
                setPasswordValue({
                  ...passwordValue,
                  currentPassword: e.target.value,
                })
              }
            />
          </S.Wrap>
        </S.FlexWrap>

        <S.FlexWrap>
          <S.Label>새 비밀번호</S.Label>
          <S.Wrap>
            <S.Input
              type="password"
              value={passwordValue.newPassword}
              onChange={e => {
                setPasswordError({
                  ...passwordError,
                  newPassword: !validatePassword(e.target.value)
                    ? '비밀번호 형식이 맞지 않습니다.'
                    : '',
                })
                setPasswordValue({
                  ...passwordValue,
                  newPassword: e.target.value,
                })
              }}
            />
            <S.ErrorMessage>{passwordError.newPassword}</S.ErrorMessage>
          </S.Wrap>
        </S.FlexWrap>
        <S.FlexWrap>
          <S.Label>새 비밀번호 확인</S.Label>
          <S.Wrap>
            <S.Input
              type="password"
              value={newPasswordCheck}
              onChange={e => {
                setPasswordError({
                  ...passwordError,
                  newPasswordCheck: !validatePasswordConfirm(
                    passwordValue.newPassword,
                    e.target.value
                  )
                    ? '비밀번호가 일치하지 않습니다.'
                    : '',
                })
                setNewPasswordCheck(e.target.value)
              }}
            />
            <S.ErrorMessage>{passwordError.newPasswordCheck}</S.ErrorMessage>
          </S.Wrap>
        </S.FlexWrap>
        <S.Button
          onClick={editPassword}
          $isActive={
            passwordValue.currentPassword !== '' &&
            passwordValue.newPassword !== '' &&
            newPasswordCheck !== '' &&
            passwordError.newPassword === '' &&
            passwordError.newPasswordCheck === ''
          }
        >
          수정하기
        </S.Button>
      </S.Container>

      {isCompleteModal && (
        <ModalWithoutButton
          content={modalContent}
          setIsModalFalse={() => setIsCompleteModal(false)}
        />
      )}

      <BottomNavigation />
    </>
  )
}

export default EditPasswordPage

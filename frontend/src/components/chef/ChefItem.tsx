import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import * as S from '../../styles/chef/ChefItem.styled'
import profile from '../../../public/images/default_profile.png'
import ModalWithTwoButton from '../ui/ModalWithTwoButton'

export interface MainChef {
  chefMemberId: number
  memberId: number
  nickname: string
  profileUrl: string
}

const ChefItem = ({ item }: { item: MainChef }) => {
  const navigate = useNavigate()

  const TOKEN: string | null = JSON.parse(
    localStorage.getItem('accessToken') ?? 'null'
  )

  const [isModal, setIsModal] = useState(false)

  const onClickIcon = () => {
    if (TOKEN) {
      navigate(`/profile/${item.memberId}`)
    } else {
      setIsModal(true)
    }
  }

  return (
    <S.Container onClick={onClickIcon}>
      <S.Image src={item.profileUrl || profile} />
      <S.Nickname>{item.nickname}</S.Nickname>
      {isModal && (
        <ModalWithTwoButton
          content="로그인이 필요한 기능입니다."
          subContent="로그인하러 가시겠습니까?"
          setIsModalFalse={() => setIsModal(false)}
          modalEvent={() => {
            setIsModal(false)
            navigate('/login')
          }}
        />
      )}
    </S.Container>
  )
}

export default ChefItem

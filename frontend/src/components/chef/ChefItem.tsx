import { useNavigate } from 'react-router-dom'
import * as S from '../../styles/chef/ChefItem.styled'
import profile from '../../../public/images/default_profile.png'

export interface MainChef {
  chefMemberId: number
  memberId: number
  nickname: string
  profileUrl: string
}

const ChefItem = ({ item }: { item: MainChef }) => {
  const navigate = useNavigate()
  return (
    <S.Container onClick={() => navigate(`/profile/${item.memberId}`)}>
      <S.Image src={item.profileUrl || profile} />
      <S.Nickname>{item.nickname}</S.Nickname>
    </S.Container>
  )
}

export default ChefItem

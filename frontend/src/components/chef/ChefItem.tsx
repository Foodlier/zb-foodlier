import * as S from '../../styles/chef/ChefItem.styled'

interface ChefItemProps {
  // eslint-disable-next-line react/no-unused-prop-types
  chefId: string | number
  nickname: string
  profileUrl: string
}

const ChefItem = ({ nickname, profileUrl }: ChefItemProps) => {
  return (
    <S.Container>
      <S.Image src={profileUrl} />
      <S.Nickname>{nickname}</S.Nickname>
    </S.Container>
  )
}

export default ChefItem

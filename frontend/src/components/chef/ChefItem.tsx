import React from 'react'
import * as S from '../../styles/chef/ChefItem.styled'

interface ChefItemProps {
  chefId: string | number
  nickname: string
  profileUrl: string
}

const ChefItem = ({ chefId, nickname, profileUrl }: ChefItemProps) => {
  return (
    <S.Container>
      <S.Image src={profileUrl} />
      <S.Nickname>{nickname}</S.Nickname>
    </S.Container>
  )
}

export default ChefItem

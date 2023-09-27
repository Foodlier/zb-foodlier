import React, { useState } from 'react'
import * as S from '../../styles/chef/ChefItem.styled'
import { palette } from '../../constants/Styles'
import styled from 'styled-components'

interface ChefItemProps {
  chefId: string | number
  nickname: string
  imagePath: string
}

const ChefItem: React.FC<ChefItemProps> = ({
  chefId,
  imagePath,
  nickname,
}) => {

  return (
    <S.Container>
      <S.Image src={imagePath} />
      <S.Nickname>{nickname}</S.Nickname>
    </S.Container>
  )
}

export default ChefItem

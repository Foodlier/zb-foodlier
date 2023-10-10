import React, { useState } from 'react'
import * as S from '../../../../styles/recipe/detail/comment/ReplyComment.styled'
// import useIcon from '../../../../hooks/useIcon'
// import { palette } from '../../../../constants/Styles'

interface Comment {
  message: string
  createdAt: string
  isDeleted: boolean
  nickname: string
  profileUrl: string
}

interface ReplyCommentProps {
  comments: Comment[]
}

function ReplyComment({ comments }: ReplyCommentProps) {
  return <S.ReplyContainer>View 1 more</S.ReplyContainer>
}

export default ReplyComment

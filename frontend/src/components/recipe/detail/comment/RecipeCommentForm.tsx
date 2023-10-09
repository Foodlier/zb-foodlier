import React, { useState } from 'react'
import * as S from '../../../../styles/recipe/detail/comment/RecipeCommentForm.styled'

interface RecipeCommentFormProps {
  onSubmit: (newComment: string) => void
}

const RecipeCommentForm = ({ onSubmit }: RecipeCommentFormProps) => {
  const [newComment, setNewComment] = useState('')

  const handleNewCommentChange = (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    setNewComment(event.target.value)
  }

  const handleNewCommentSubmit = () => {
    if (newComment.trim() === '') {
      return
    }
    onSubmit(newComment)
    setNewComment('')
  }

  return (
    <S.CommentForm>
      <S.CommentInput
        type="text"
        placeholder="댓글을 입력하세요"
        value={newComment}
        onChange={handleNewCommentChange}
      />
      <S.CommentSubmit type="button" onClick={handleNewCommentSubmit}>
        등록
      </S.CommentSubmit>
    </S.CommentForm>
  )
}

export default RecipeCommentForm

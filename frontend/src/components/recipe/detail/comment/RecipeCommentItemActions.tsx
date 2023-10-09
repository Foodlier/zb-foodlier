import React from 'react'
import styled from 'styled-components'
import CommonButton from '../../../ui/Button'

const CommentItemActions = styled.div`
  display: flex;
  justify-content: flex-end;
  margin-top: 1rem;
`

interface RecipeCommentItemActionsProps {
  onEditClick: () => void
  onDeleteClick: () => void
  isReply: boolean
}

const RecipeCommentItemActions: React.FC<RecipeCommentItemActionsProps> = ({
  onEditClick,
  onDeleteClick,

  isReply,
}) => {
  return (
    <CommentItemActions>
      {isReply && (
        <CommonButton size="small" color="main">
          대댓글 작성
        </CommonButton>
      )}
      <CommonButton onClick={onEditClick} size="small" color="divider">
        수정
      </CommonButton>
      <CommonButton onClick={onDeleteClick} size="small" color="divider">
        삭제
      </CommonButton>
    </CommentItemActions>
  )
}

export default RecipeCommentItemActions

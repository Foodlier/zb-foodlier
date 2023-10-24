import styled from 'styled-components'
import { palette } from '../../../../constants/Styles'

export const CommentItemWrapper = styled.div<{ isReply?: boolean }>`
  border-bottom: 1px solid #ccc;
  margin: 1rem 0;
  padding: 2.5rem 2rem;
  background-color: ${({ isReply }) => (isReply ? '#ddd' : '#fff')};
  ${({ isReply }) => isReply && 'margin-left: 20px;'}
`

export const CommentHeader = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 1rem;
`

export const CommentUserInfo = styled.div`
  display: flex;
  align-items: center;
`

export const UserImg = styled.img`
  width: 5rem;
  height: 5rem;
  border-radius: 50%;
`

export const UserNickname = styled.p`
  padding-left: 1rem;
`

export const CommentDate = styled.p`
  display: flex;
  align-items: center;
  color: ${palette.textSecondary};
`
export const CommentEdit = styled.form`
  width: 100%;
  padding: 1rem 0;
`
export const CommentEditInput = styled.textarea`
  width: 100%;
  height: 10rem;
  max-height: auto;
  flex: 1 1 auto;
  padding: 0 2rem;
  border-radius: 1rem;
  margin: 0 0 1rem;
  border: 0.1rem solid ${palette.divider};
`

export const CommentButtonWrap = styled.div`
  display: flex;
  justify-content: flex-end;
`
export const CommentContent = styled.div`
  width: 100%;
`
export const CommentActions = styled.div`
  width: 100%;
`
export const CommentReplyForm = styled.div`
  width: 100%;
`
export const CommentReplies = styled.div`
  width: 100%;
`

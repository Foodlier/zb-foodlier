import styled from 'styled-components'
import { breakpoints, palette, typography } from '../../constants/Styles'

export const Container = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 100%;
  border-left: 1px solid ${palette.divider};
  /* padding-bottom: 6rem; */
  max-height: calc(100% - 260px);
  ${breakpoints.large} {
    padding-bottom: 0;
    max-height: fit-content;
  }
`

export const ChattingHeader = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: ${palette.white};
  padding: 1.4rem 2rem;
  margin: 1rem;
  border-radius: 1rem;
  box-shadow: 1px 1px 4px ${palette.shadow};

  ${breakpoints.large} {
    padding: 2rem 5rem;
  }
`

export const ProfileImage = styled.img<{ $size: number }>`
  width: ${props => props.$size}rem;
  height: ${props => props.$size}rem;
  border-radius: 5rem;
  margin-right: 1rem;
  border: 1px solid black;
  object-fit: cover;
`

export const Nickname = styled.span`
  font-size: ${typography.mobile.content};

  ${breakpoints.large} {
    font-size: ${typography.web.content};
  }
`

export const ChattingMessage = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column-reverse;
  align-items: center;
  /* justify-content: flex-end; */
  padding: 1rem 3rem;
  overflow-y: auto;
  ${breakpoints.large} {
    padding-bottom: 0;
    height: calc(100% -163px);
  }
`

export const WrapMessage = styled.div<{ $isMe: boolean }>`
  align-self: ${props => (props.$isMe ? 'flex-end' : 'flex-start')};
  display: flex;
  flex-direction: ${props => (props.$isMe ? 'row-reverse' : 'row')};
  align-items: flex-end;
  margin-bottom: 1rem;
`

export const Message = styled.div<{ $isMe: boolean }>`
  position: relative;
  max-width: 25rem;
  background-color: ${props => (props.$isMe ? palette.main : palette.white)};
  color: ${props => (props.$isMe ? palette.white : palette.textPrimary)};
  padding: 1rem;
  border-radius: 1rem;
  font-size: ${typography.mobile.desc};

  ${breakpoints.large} {
    max-width: 40rem;
    font-size: 1.4rem;
  }
`

export const Suggestion = styled.div<{ $isMe: boolean }>`
  max-width: 25rem;
  border: 2px solid ${palette.yellow};
  background-color: ${palette.white};
  padding: 1rem;
  border-radius: 10px;
  font-size: ${typography.mobile.desc};
  box-sizing: content-box;
  box-shadow: 0px 2px 6px 2px rgba(89, 97, 104, 0.1);
  ${breakpoints.large} {
    max-width: 40rem;
    font-size: 1.4rem;
  }
`

export const SuggestionTitle = styled.p`
  font-weight: bold;
`

export const SuggestionButton = styled.button<{ $isAccept: boolean }>`
  width: 50%;
  border-radius: 5px;
  background-color: ${props =>
    props.$isAccept ? palette.main : palette.divider};
  color: ${props => (props.$isAccept ? palette.white : palette.textPrimary)};
`

export const MessageTime = styled.span`
  font-size: 1rem;
  margin: 0 0.5rem;

  color: ${palette.textSecondary};

  ${breakpoints.large} {
    font-size: ${typography.mobile.desc};
    margin: 0 1rem;
  }
`

export const WrapInput = styled.div`
  position: fixed;
  bottom: 10rem;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: white;
  padding: 1rem 1.5rem;

  ${breakpoints.large} {
    position: static;
    padding: 2rem 5rem;
  }
`

export const Input = styled.input`
  border: 1px solid ${palette.divider};
  padding: 0.8rem;
  border-radius: 1rem;
  width: 84%;
  font-size: ${typography.mobile.desc};

  ${breakpoints.large} {
    font-size: 1.4rem;
    padding: 1rem;
    width: 86%;
  }
`

export const WrapDate = styled.div`
  background-color: ${palette.white};
  color: ${palette.textPrimary};
  border: 1px solid ${palette.divider};
  padding: 0.5rem 2rem;
  border-radius: 1.5rem;
  font-size: ${typography.mobile.desc};
`

export const Button = styled.button`
  background-color: ${palette.main};
  color: ${palette.white};
  border-radius: 1rem;
  padding: 0.8rem 1.5rem;
  font-size: ${typography.mobile.desc};

  ${breakpoints.large} {
    padding: 1rem 2rem;
    font-size: 1.4rem;
  }
`

export const Wrap = styled.div`
  display: flex;
  gap: 10px;
  align-items: center;
`

export const FlexAlignCenter = styled.div`
  display: flex;
  gap: 10px;
  align-items: center;
`

export const ProposalButton = styled.button`
  border: 1px solid ${palette.main};
  padding: 0.8rem 1rem;
  border-radius: 1rem;
  background-color: ${palette.white};
  color: ${palette.main};
  font-size: 1rem;
  font-weight: 800;

  ${breakpoints.large} {
    font-size: 1.4rem;
    padding: 0.8rem 1.4rem;
  }
`

export const ExitButton = styled.button`
  border: 1px solid ${palette.textDisablePlace};
  padding: 0.8rem 1rem;
  border-radius: 1rem;
  background-color: ${palette.white};
  color: ${palette.textDisablePlace};
  font-size: 1rem;
  font-weight: 800;
  margin-left: 1rem;

  ${breakpoints.large} {
    font-size: 1.4rem;
    padding: 0.8rem 1.4rem;
  }
`

export const ObserverDiv = styled.div`
  width: 50px;
  height: 50px;
  background-color: white;
  border-radius: 50%;
  opacity: 0.1;
`

export const ExitMessage = styled.p`
  background-color: yellowgreen;
  padding: 1% 2%;
  border-radius: 10px;
  background-color: ${palette.textDisablePlace};
  color: ${palette.white};
  margin-bottom: 10px;
`
